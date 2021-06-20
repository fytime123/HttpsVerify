package top.corobot.network

data class HttpResult<T>(val code: Int, val message: String?, val data: T?) {
    companion object {
        val OK = 0
        val ERROR = 100
    }
}