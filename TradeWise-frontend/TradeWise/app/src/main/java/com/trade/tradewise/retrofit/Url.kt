package com.star.starmedicahub.retrofit

import com.trade.tradewise.frgement.ChangePasswordFrgement

object Url {
    const val LOGIN = "auth_login/login/"
    const val SIGNUP = "auth_login/register/";
    const val PROFILE_ORGINAIZOR="portfolio_organizer/insert_account/"
    const val  get_active_portfolio_accounts="portfolio_organizer/get_active_portfolio_accounts"
    const val get_user_profile="auth_login/customer_profile_detail"
    const val edit_customore="auth_login/edit_customer"
    const val ChangePassword="auth_login/change_password"
    const val TRADE_LOGS="trade_log/get_trade_logs/"
    const val UPLOADFILE="upload_file/file"
    const val DASHBOARDWIN="dashboard/win_percentage/"
    const val Profitloss="dashboard/profit_loss/"
    const val ProfitFactor="dashboard/profit_factor/"
    const val AVG_WIN_LOSS="dashboard/avg_win_loss_ratio/"
    const val ACCOUNT_LIST="analytics_report/fetch-account-names/"
    const val ANALYTICS_REPORT="analytics_report/fetch-account-symbols/"
    const val DOLLAR_VALUE="general_setting/tradesettings/create"
}
