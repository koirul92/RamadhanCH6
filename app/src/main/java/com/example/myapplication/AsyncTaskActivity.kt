package com.example.myapplication

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.databinding.ActivityAsyncTaskBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONException

class AsyncTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAsyncTaskBinding

    lateinit var context: Context
    var QuestionList: MutableList<Question> = ArrayList()
    var index = -1
    var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAsyncTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        binding.btnNext.isEnabled = false
        binding.btnNext.alpha = 0.5.toFloat()
        getQuestions().execute()
    }

    fun UpdateQuestion() {
        val selected = binding.rgChoice.checkedRadioButtonId
        if (selected == -1) {
            Toast.makeText(this, "Please select answer.", Toast.LENGTH_SHORT).show()
            return
        }
        if (index < QuestionList.size) {
            when (selected) {
                binding.rbChoice1.id -> {
                    if (QuestionList[index].Answer == 1)
                        score++
                }
                binding.rbChoice2.id -> {
                    if (QuestionList[index].Answer == 2)
                        score++
                }
                binding.rbChoice3.id -> {
                    if (QuestionList[index].Answer == 3)
                        score++
                }
                binding.rbChoice4.id -> {
                    if (QuestionList[index].Answer == 4)
                        score++
                }
            }
            index++
            if (index < QuestionList.size) {
                binding.tvQuestion.text = QuestionList[index].Question
                binding.rbChoice1.text = QuestionList[index].Option1
                binding.rbChoice2.text = QuestionList[index].Option2
                binding.rbChoice3.text = QuestionList[index].Option3
                binding.rbChoice4.text = QuestionList[index].Option4
                binding.rgChoice.clearCheck()
                if((index+1) == QuestionList.size)
                    binding.btnNext.text = "Finish"
            } else {
                val dialog = AlertDialog.Builder(context)
                dialog.setTitle("Your Score")
                dialog.setMessage("You have answered " + score + " out of " + QuestionList.size + "Questions correctly.")
                dialog.setPositiveButton("Close") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                    finish()
                }
                dialog.show()

            }
        }
    }

    internal inner class getQuestions : AsyncTask<Void, Void, String>() {

        lateinit var progressDialog: ProgressDialog
        var hasInternet = false

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Downloading Questions...")
            progressDialog.setCancelable(false)
            progressDialog.show()
        }

        override fun doInBackground(vararg p0: Void?): String {
            if (isNetworkAvailable()) {
                hasInternet = true
                val client = OkHttpClient()
                val url = "https://raw.githubusercontent.com/koirul92/tes/master/question.json"
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                return response.body()?.string().toString()
            } else {
                return ""
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressDialog.dismiss()

            if (hasInternet) {
                try {
                    val resultArray = JSONArray(result)

                    for (i in 0..(resultArray.length() - 1)) {
                        val currentObject = resultArray.getJSONObject(i)
                        val obj = Question()
                        obj.Question = currentObject.getString("Question")
                        obj.Option1 = currentObject.getString("Option1")
                        obj.Option2 = currentObject.getString("Option2")
                        obj.Option3 = currentObject.getString("Option3")
                        obj.Option4 = currentObject.getString("Option4")
                        obj.Answer = currentObject.getInt("Answer")
                        QuestionList.add(obj)
                    }

                    if (index == -1) {
                        index++
                        binding.tvQuestion.text = QuestionList[index].Question
                        binding.rbChoice1.text = QuestionList[index].Option1
                        binding.rbChoice2.text = QuestionList[index].Option2
                        binding.rbChoice3.text = QuestionList[index].Option3
                        binding.rbChoice4.text = QuestionList[index].Option4
                    }

                    binding.btnNext.isEnabled = true
                    binding.btnNext.alpha = 1.toFloat()

                    binding.btnNext.setOnClickListener {
                        UpdateQuestion()
                    }
                } catch (e: JSONException) {

                }
            }

        }

    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}