package com.andes.metamon.core.data.repository

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.andes.metamon.BuildConfig
import com.andes.metamon.MetamonApplication.Companion.s3Client
import com.andes.metamon.MetamonApplication.Companion.transferUtility
import com.andes.metamon.auth.data.remote.model.request.ApiCertificateRequest
import com.andes.metamon.core.data.exception.RetrofitFailureStateException
import com.andes.metamon.core.data.remote.call_adapter.NetworkState
import com.andes.metamon.core.data.remote.data_source.RemoteCertificateDataSource
import com.andes.metamon.core.domain.model.CertificateRequest
import com.andes.metamon.core.domain.repository.CertificateRepository
import com.andes.metamon.logging.Logger
import java.io.File
import javax.inject.Inject

class CertificateRepositoryImpl @Inject constructor(
    private val remoteCertificateDataSource: RemoteCertificateDataSource
) : CertificateRepository {
    override suspend fun postCertificate(certificateRequest: CertificateRequest): Result<String> {
        val response = remoteCertificateDataSource.postCertificate(
            ApiCertificateRequest(
                platform = certificateRequest.platform,
                nickname = certificateRequest.nickname,
                imageUrl = certificateRequest.imageUrl
            )
        )
        when (response) {
            is NetworkState.Success -> return Result.success(
                response.body.message
            )
            is NetworkState.Failure -> return Result.failure(
                RetrofitFailureStateException(
                    response.error,
                    response.code
                )
            )
            is NetworkState.NetworkError -> Logger.d(response.error.toString())
            is NetworkState.UnknownError -> Logger.d(response.t.toString())
        }
        return Result.failure(IllegalStateException("NetworkError or UnKnownError please check timber"))
    }

    /**
     * AWS S3 ????????? ????????? ???????????????
     * ??????????????? ???????????? ????????? URL ??? ???????????? ???????????? ?????? ?????????
     * ??????????????? `StorageException` ????????? ?????????.
     *
     * @author onseok
     * @param file = ?????????????????? ?????? ??????
     * @param onResult = ????????? ??? callback function
     */
    override suspend fun uploadAndDownloadFile(
        folderName: String,
        userId: Long,
        fileName: String,
        file: File,
        onResult: (String) -> Unit
    ) {
        val uploadObserver = transferUtility.upload(
            "${BuildConfig.AWS_S3_BUCKET_NAME}/$folderName/$userId",
            fileName,
            file
        )
        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED) {
                    Logger.d("aws s3 ????????? ??????")
                    //???????????? ????????? url ????????????
                    onResult(getUploadImgUrl(folderName, userId, file))
                } else if (state == TransferState.FAILED) {
                    Logger.d("aws s3 ????????? ??????")
                    onResult("aws s3 ????????? ?????? : ${state.name}")
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {}

            override fun onError(id: Int, ex: Exception?) {
                Logger.d("UPLOAD ERROR - - ID: $id - - EX:" + ex.toString())
                onResult("aws s3 ?????? ??????: $ex")
            }
        })
    }

    /**
     *???????????? ????????? url ???????????? ?????????
     */
    private fun getUploadImgUrl(
        folderName: String,
        userId: Long,
        file: File
    ): String {
        val uploadUrl = s3Client.getResourceUrl(
            "${BuildConfig.AWS_S3_BUCKET_NAME}/$folderName/$userId",
            file.name
        )
        return uploadUrl?.let {
            return@let it
        }.orEmpty()
    }
}
