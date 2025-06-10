package com.trade.tradewise.frgement

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.star.starmedicahub.retrofit.Constance
import com.star.starmedicahub.retrofit.Retrofitresponse
import com.star.starmedicahub.retrofit.TokenProvider
import com.star.starmedicahub.retrofit.Url
import com.trade.tradewise.ProfileAdapter
import com.trade.tradewise.activity.HomeActivity
import com.trade.tradewise.databinding.DialogAddStockBinding
import com.trade.tradewise.databinding.FragmentProtfolimanagerBinding
import com.trade.tradewise.model.ProfileOrginizationModel
import org.json.JSONObject

class ProtfolimanagerFragment : Fragment() {

    private lateinit var _binding: FragmentProtfolimanagerBinding
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProtfolimanagerBinding.inflate(inflater, container, false)

        callAPi()

        binding.fab.setOnClickListener {
            showStockDialog(requireContext(), isEdit = false)
        }

        return binding.root
    }

    private fun callAPi() {
        binding.progress.visibility = View.VISIBLE
        val context = requireActivity() as? HomeActivity ?: return
        val json = JSONObject()

        context.callWeb(
            Constance.POST,
            Url.get_active_portfolio_accounts + "/${TokenProvider.getUserId()}/",
            json.toString(),
            object : Retrofitresponse {
                override fun onSucess(response: String?, error: String?) {
                    binding.progress.visibility = View.GONE
                    val jsonObject = JSONObject(response)
                    val list = Gson().fromJson<List<ProfileOrginizationModel>>(
                        jsonObject.getJSONArray("portfolio_accounts").toString(),
                        object : TypeToken<List<ProfileOrginizationModel>>() {}.type
                    )

                    if (list.isNotEmpty()) {
                        TokenProvider.setAccountId(list[0].aCCOUNTID.toString())
                    }

                    setupRecyclerView(list)
                }

                override fun onFailer(response: String?, error: String?) {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(activity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupRecyclerView(profileList: List<ProfileOrginizationModel>) {
        val adapter = ProfileAdapter(profileList) { profile ->
            showStockDialog(requireContext(), isEdit = true, profile = profile)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
    }

    private fun showStockDialog(context: Context, isEdit: Boolean, profile: ProfileOrginizationModel? = null) {
        val binding = DialogAddStockBinding.inflate(LayoutInflater.from(context))

        if (isEdit && profile != null) {
            binding.etName.setText(profile.aCCOUNTNAME)
            binding.etStockType.setText(profile.sTOCKTYPE)
            binding.etProfitMethod.setText(profile.pROFITCALCULATIONMETHOD)
            binding.etBalance.setText(profile.iNITIALDEPOSIT ?: "0.0")
            binding.etDescription.setText(profile.bALANCEDESCRIPTION)
        }

        val builder = AlertDialog.Builder(context)
            .setTitle(if (isEdit) "Edit Stock Info" else "Add Stock Info")
            .setView(binding.root)
            .setPositiveButton(if (isEdit) "Update" else "Add", null)
            .setNegativeButton("Cancel", null)

        if (isEdit) {
            builder.setNeutralButton("Delete") { _, _ ->
                showDeleteConfirmation(context, profile)
            }
        }

        val dialog = builder.create()

        dialog.setOnShowListener {
            val actionButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            actionButton.setOnClickListener {
                val name = binding.etName.text.toString()
                val stockType = binding.etStockType.text.toString()
                val profitMethod = binding.etProfitMethod.text.toString()
                val balance = binding.etBalance.text.toString()
                val description = binding.etDescription.text.toString()

                if (name.isEmpty() || stockType.isEmpty()) {
                    Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val activity = requireActivity() as? HomeActivity ?: return@setOnClickListener

                if (isEdit && profile != null) {
                    val json = JSONObject().apply {
                        put("fields_to_update", JSONObject().apply {
                            put("name", name)
                            put("stock_type", stockType)
                            put("profit_calculation_method", profitMethod)
                            put("balance", balance)
                            put("description", description)
                        })
                    }

                    val updateUrl =
                        "https://tradewise-backend-14.onrender.com/portfolio_organizer/update_account/${profile.aCCOUNTID}/"

                    activity.callWeb(Constance.POST, updateUrl, json.toString(), object : Retrofitresponse {
                        override fun onSucess(response: String?, error: String?) {
                            Toast.makeText(context, "Updated successfully!", Toast.LENGTH_SHORT).show()
                            callAPi()
                            dialog.dismiss()
                        }

                        override fun onFailer(response: String?, error: String?) {
                            Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    val json = JSONObject().apply {
                        put("Name", name)
                        put("Stock_Type", stockType)
                        put("Profit_Calculation_Method", profitMethod)
                        put("Balance", balance.toDoubleOrNull() ?: 0.0)
                        put("Description", description)
                        put("status", true)
                        put("Created_By", "admin")
                    }

                    val activity = requireActivity() as? HomeActivity ?: return@setOnClickListener
                    val url = Url.PROFILE_ORGINAIZOR + "${TokenProvider.getUserId()}/"

                    activity.callWeb(Constance.POST, url, json.toString(), object : Retrofitresponse {
                        override fun onSucess(response: String?, error: String?) {
                            callAPi()
                            dialog.dismiss()
                        }

                        override fun onFailer(response: String?, error: String?) {
                            Toast.makeText(context, "Creation failed", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }

        dialog.show()
    }

    private fun showDeleteConfirmation(context: Context, profile: ProfileOrginizationModel?) {
        if (profile == null || profile.aCCOUNTID.isNullOrEmpty()) {
            Toast.makeText(context, "Invalid account ID", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(context)
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete this portfolio account?")
            .setPositiveButton("Delete") { _, _ ->
                val deleteUrl =
                    "https://tradewise-backend-14.onrender.com/portfolio_organizer/delete_account/${TokenProvider.getUserId()}/${profile.aCCOUNTID}/"
                val activity = requireActivity() as? HomeActivity ?: return@setPositiveButton

                activity.callWeb(Constance.POST, deleteUrl, "{}", object : Retrofitresponse {
                    override fun onSucess(response: String?, error: String?) {
                        Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()
                        callAPi()
                    }

                    override fun onFailer(response: String?, error: String?) {
                        Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show()
                        Log.e("DeleteError", response ?: "null")
                    }
                })
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
