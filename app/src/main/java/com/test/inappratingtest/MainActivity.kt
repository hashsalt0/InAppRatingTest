package com.test.inappratingtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode

class MainActivity : AppCompatActivity() {

    private val manager by lazy { ReviewManagerFactory.create(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener{
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviewInfo = task.result
                    val flow = reviewInfo?.let { it1 -> manager.launchReviewFlow(this, it1) }
                    flow?.addOnCompleteListener { _ ->
                        if(flow.isSuccessful){
                            Toast.makeText(this, "rating was successful", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this, "could not rate the app", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    val errorCode = when (val exception = task.exception) {
                        is ReviewException -> {
                            exception.errorCode
                        }
                        is RuntimeExecutionException -> {
                            -9999
                        }
                        else -> {
                            9999
                        }
                    }
                    Toast.makeText(this, "could not rate the app $errorCode", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}