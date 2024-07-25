package com.example.constraintlayout

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import java.text.DecimalFormat
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech

    private var ttsSucess: Boolean = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        atualizarTexto()
        compartilharConta()
        // Initialize TTS engine
        tts = TextToSpeech(this, this)

    }

    fun atualizarTexto(){
        var valueCount: TextView = findViewById(R.id.valueCount)
        var valuePeople: TextView = findViewById(R.id.valuePeople)
        var valueTotal: TextView = findViewById(R.id.valueTotal)


        valuePeople.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if (s.isNotEmpty() && valueCount.text.isNotEmpty()  && valuePeople.text.isNotEmpty() && !valuePeople.text.equals("0") ) {
                    val valueDouble = valueCount.text.toString().toDouble()
                    val pessoaDouble = valuePeople.text.toString().toDouble()
                    valueTotal.text = "${calcular(valueDouble,pessoaDouble)} pra cada"
                } else{
                    valueTotal.text = ""
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        valueCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && valueCount.text.isNotEmpty() && valuePeople.text.isNotEmpty() && !valuePeople.text.equals("0") ) {
                    val valueDouble = valueCount.text.toString().toDouble()
                    val pessoaDouble = valuePeople.text.toString().toDouble()
                    valueTotal.text = "${calcular(valueDouble,pessoaDouble)} pra cada"

                } else{
                    valueTotal.text = ""
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })


    }

    fun calcular(value : Double, people : Double):String{
        val resultValue = value/people
        val regex = DecimalFormat("##.##")
        return "R$ ${regex.format(resultValue)}"
    }



    fun compartilharConta(){
            val shareBtn : ImageButton = findViewById(R.id.floatingActionButton)


        shareBtn.setOnClickListener{
                val result : TextView = findViewById(R.id.valueTotal)


                if(result.text.toString().isNotEmpty()) {

                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, mensagem())
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)

                    startActivity(shareIntent)
                }
            }
    }

    private fun mensagem(): CharSequence? {
        val valueCount : TextView = findViewById(R.id.valueCount)
        val valuePeople : TextView = findViewById(R.id.valuePeople)
        if(valuePeople.text.isNotEmpty() && valueCount.text.isNotEmpty()) {
            val valueString = valueCount.text.toString()
            val pessoaString = valuePeople.text.toString()
            val resultFormatted = calcular(valueString.toDouble(), pessoaString.toDouble())
            return "E o PIX PAPAI?! A conta total deu $valueString reais pra um total de $pessoaString pessoas que fica $resultFormatted pra cada um"
        }
        return "Faltam campos serem preenchidos"
    }

    fun clickFalar(v: View){
        Log.v("PDM24", "$v pressed")
        tts.speak(mensagem(), TextToSpeech.QUEUE_FLUSH, null, null)

    }
    override fun onDestroy() {
            // Release TTS engine resources
            tts.stop()
            tts.shutdown()
            super.onDestroy()
        }

    override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                // TTS engine is initialized successfully
                tts.language = Locale.getDefault()
                ttsSucess=true
                Log.d("PDM23","Sucesso na Inicialização")
            } else {
                // TTS engine failed to initialize
                Log.e("PDM23", "Failed to initialize TTS engine.")
                ttsSucess=false
            }
        }


}

