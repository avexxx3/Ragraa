import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.avex.ragraa.data.CaptchaSolver
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.data.dataclasses.LoginRequest
import com.avex.ragraa.network.RagraaApi

class PeriodicWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val key = CaptchaSolver.solveCaptcha()
            val loginReq = LoginRequest(Datasource.rollNo, Datasource.password, key.getOrThrow())
            RagraaApi.loginFlex(loginReq)

             TODO("Check new updates")


            TODO("Send as notification")

            Result.success(workDataOf("key" to "value"))
        } catch (e: Exception) {
            Result.failure()
        }
    }
}