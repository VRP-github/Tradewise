package com.trade.tradewise.frgement

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.star.starmedicahub.retrofit.Constance
import com.star.starmedicahub.retrofit.Retrofitresponse
import com.star.starmedicahub.retrofit.TokenProvider
import com.star.starmedicahub.retrofit.Url
import com.trade.tradewise.activity.HomeActivity
import com.trade.tradewise.activity.LoginActivity
import com.trade.tradewise.databinding.FragmentUserProfileBinding
import org.json.JSONObject

class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        binding.profileImage.setImageURI(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        setupViews()
        callApi() // Initial fetch

        return binding.root
    }

    private fun setupViews() {
        binding.profileImage.setOnClickListener {
            activityResultLauncher.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            saveProfile()
        }

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun saveProfile() {
        binding.progress.visibility = View.VISIBLE
        val homeActivity = activity as HomeActivity

        val jsonObject = JSONObject().apply {
            put("First_Name", binding.etName.text.toString())
            put("Last_Name", binding.etLastName.text.toString())
            put("Email", binding.etEmail.text.toString())
        }

        homeActivity.callWeb(
            Constance.POST,
            Url.edit_customore + "/${TokenProvider.getUserId()}/",
            jsonObject.toString(),
            object : Retrofitresponse {
                override fun onSucess(response: String?, error: String?) {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    callApi()
                }

                override fun onFailer(response: String?, error: String?) {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(requireContext(), error ?: "Update failed!", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    // 🔥 Now public so other fragments can call it too!
    fun callApi() {
        binding.progress.visibility = View.VISIBLE
        val homeActivity = activity as HomeActivity

        val jsonObject = JSONObject()

        homeActivity.callWeb(
            Constance.POST,
            Url.get_user_profile + "/${TokenProvider.getUserId()}/",
            jsonObject.toString(),
            object : Retrofitresponse {
                override fun onSucess(response: String?, error: String?) {
                    binding.progress.visibility = View.GONE
                    val jsonResponse = JSONObject(response)
                    binding.etName.setText(jsonResponse.optString("First_Name"))
                    binding.etLastName.setText(jsonResponse.optString("Last_Name"))
                    binding.etEmail.setText(jsonResponse.optString("Email"))
                }

                override fun onFailer(response: String?, error: String?) {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(requireContext(), "Failed to load profile.", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun performLogout() {
        // ✅ Without clearToken
        TokenProvider.setToken("")
        TokenProvider.setUserId("")

        Toast.makeText(requireContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show()

        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
