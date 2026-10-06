package com.bignerdranch.android.json_pr_31_taztdinova

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private val gson = Gson()
    private var currentJson: String? = null

    private lateinit var editName: AppCompatEditText
    private lateinit var editPrice: AppCompatEditText
    private lateinit var editTags: AppCompatEditText
    private lateinit var textJson: AppCompatTextView
    private lateinit var textObject: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editName = findViewById(R.id.edit_product_name)
        editPrice = findViewById(R.id.edit_product_price)
        editTags = findViewById(R.id.edit_product_tags)
        textJson = findViewById(R.id.text_json)
        textObject = findViewById(R.id.text_object)

        val btnToJson: AppCompatButton = findViewById(R.id.btn_to_json)
        val btnFromJson: AppCompatButton = findViewById(R.id.btn_from_json)
        val btnClear: AppCompatButton = findViewById(R.id.btn_clear)

        val startProduct = Product(
            "Android для начинающих",
            1200.0,
            listOf("книга", "мобильная разработка", "kotlin")
        )
        fillFields(startProduct)
        toJson(startProduct)

        btnToJson.setOnClickListener {
            val product = readProduct()
            if (product == null) {
                Toast.makeText(this, R.string.error_fields, Toast.LENGTH_SHORT).show()
            } else {
                toJson(product)
            }
        }

        btnFromJson.setOnClickListener {
            val json = currentJson
            if (json == null) {
                Toast.makeText(this, R.string.error_no_json, Toast.LENGTH_SHORT).show()
            } else {
                fromJson(json)
            }
        }

        btnClear.setOnClickListener {
            editName.setText("")
            editPrice.setText("")
            editTags.setText("")
            textJson.text = ""
            textObject.text = ""
            currentJson = null
        }
    }

    private fun fillFields(product: Product) {
        editName.setText(product.name)
        editPrice.setText(product.price.toString())
        editTags.setText(product.tags.joinToString(", "))
    }

    private fun readProduct(): Product? {
        val name = editName.text.toString().trim()
        val price = editPrice.text.toString().toDoubleOrNull()

        if (name.isEmpty() || price == null) {
            return null
        }

        val tags = mutableListOf<String>()
        for (tag in editTags.text.toString().split(",")) {
            val cleanTag = tag.trim()
            if (cleanTag.isNotEmpty()) {
                tags.add(cleanTag)
            }
        }

        return Product(name, price, tags)
    }

    private fun toJson(product: Product) {
        val json = gson.toJson(product)
        currentJson = json
        textJson.text = json
        textObject.text = ""
        Log.i("JsonTaztdinova", "toJson: $json")
    }

    private fun fromJson(json: String) {
        try {
            val product = gson.fromJson(json, Product::class.java)
            val result = StringBuilder()
            result.append("Товар: ").append(product.name).append("\n")
            result.append("Стоимость: ").append(product.price).append("\n")
            result.append("Теги: ").append(product.tags.joinToString(", "))
            textObject.text = result.toString()
            Log.i("JsonTaztdinova", "fromJson: $result")
        } catch (e: Exception) {
            textObject.text = "Ошибка: ${e.message}"
            Log.e("JsonTaztdinova", "fromJson error", e)
        }
    }
}