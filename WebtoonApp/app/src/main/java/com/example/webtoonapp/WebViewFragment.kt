package com.example.webtoonapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.webtoonapp.databinding.FragmentWebviewBinding

class WebViewFragment(private val position: Int, private val webViewUrl: String): Fragment() {

    var listener: OnTabLayoutNameChanged? = null

    private lateinit var binding: FragmentWebviewBinding

    companion object {
        const val SHARED_PREFERENCES = "WEB_HISTORY"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebviewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.webViewClient = WebtoonWebViewClient(binding.progressBar) { url ->
            activity?.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)?.edit {
                putString("tab$position", url)
            }
        }
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(webViewUrl)

        binding.backToLastButton.setOnClickListener {
            //Fragment는 부모의 activity 갖고 올 수 있음
            val sharedPreferences = activity?.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val url = sharedPreferences?.getString("tab$position", "")
            if(url.isNullOrEmpty()) {
                Toast.makeText(context, "마지막 저장 시점이 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                binding.webView.loadUrl(url)
            }
        }

        binding.changeTabNameButton.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            val editText = EditText(context)

            dialog.setView(editText)
            dialog.setPositiveButton("저장") { _, _ ->
                //람다 인자는 인자로 안넣고 함수로 뺄 수 있음.
                activity?.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)?.edit {
                    putString("tab${position}_name", editText.text.toString())
                    //이후 MainActivity에 변경된 탭 알려줘야 함
                    listener?.nameChanged(position, editText.text.toString())
                }
            }
            dialog.setNegativeButton("취소") { dialogInterface, _ ->
                dialogInterface.cancel()
            }

            dialog.show()

        }
    }

    fun canGoBack(): Boolean {
        return binding.webView.canGoBack()
    }

    fun goBack() {
        binding.webView.goBack()
    }
}

interface OnTabLayoutNameChanged {
    //Adapter에서 mainActivity가 리스너인거 설정
    fun nameChanged(position: Int, name: String)
}