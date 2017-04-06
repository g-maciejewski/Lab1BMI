package pl.edu.pwr.grzegorzmaciejewski.lab1.lab1


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ShareActionProvider
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.content_main.*
import pl.edu.pwr.grzegorzmaciejewski.lab1.lab1.R.array.AvaiableUnits
import java.lang.NumberFormatException
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private var counter: ICountBMI = CountBMIForKgM()
    private var shareActionProvide: ShareActionProvider by Delegates.notNull()
    internal var listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
            setBmiCounter(position)
            checkInputData()
        }

        override fun onNothingSelected(parent: AdapterView<*>) {}
    }

        private val textWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {
            checkInputData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        readSavedData()
        createSpinner()
        btnCount.setOnClickListener { showMyBMI() }
        etMass.addTextChangedListener(textWatcher)
        etHeight.addTextChangedListener(textWatcher)
        btnSave.setOnClickListener { handleSavingInputData() }
    }
    private fun setBmiCounter(selectedItemPosition: Int) {
        counter =
                if (selectedItemPosition == 0)
                    CountBMIForKgM()
                else
                    CountBMIForLbIn()
    }

///////PRZY URUCHAMIANIU
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val textColor = savedInstanceState.getInt("Color")
        val bmi=savedInstanceState.getString("BMI")
        tvBmiResult.text=bmi
        tvBmiResult.setTextColor(textColor)
    }

     override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val textColor = tvBmiResult.textColors.defaultColor
         val bmi=tvBmiResult.text.toString()
         outState.putString("BMI",bmi)
        outState.putInt("Color",textColor)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        findShareActionProvider(menu)
        return true
    }

    private fun readSavedData() {
        val inputData = getPreferences(Context.MODE_PRIVATE)
        etMass.setText(inputData.getString("Mass", ""))
        etHeight.setText(inputData.getString("Height", ""))
        spinner.setSelection(inputData.getInt("Spinner",0))
    }
    private fun createSpinner() {
        val adapter = ArrayAdapter.createFromResource(this, AvaiableUnits, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = listener
    }
////////////////////////////////////
    ////LICZENIE
    ////////////
    private fun showMyBMI() {
        calculateBMI()
        invalidateOptionsMenu()
    }
    fun calculateBMI() {
        hideKeyboard(this)
        if (inputIsGood()) {
            if (spinner.selectedItemPosition == 0) {
                counter = CountBMIForKgM()
                countBMIEurope(java.lang.Float.parseFloat(etMass.text.toString()), java.lang.Float.parseFloat(etHeight.text.toString()))
            } else {
                counter = CountBMIForLbIn()
                countBMIMurica(java.lang.Float.parseFloat(etMass.text.toString()), java.lang.Float.parseFloat(etHeight.text.toString()))
            }
        } else
            tvBmiResult.setText(R.string.invalidInput)
    }

    private fun countBMIEurope(mass: Float?, height: Float?) {
        val bmi: Float
        try {
            bmi = counter.countBMI(mass!!, height!!)
            setResult(bmi)
        } catch (e: IllegalArgumentException) {
            tvBmiResult.setText(R.string.invalidInput)
        }
    }

    private fun countBMIMurica(mass: Float?, height: Float?) {
        val bmi: Float
        try {
            bmi = counter.countBMI(mass!!, height!!)
            setResult(bmi)
        } catch (e: IllegalArgumentException) {
            tvBmiResult.setText(R.string.invalidInput)
        }
    }
////////////////////////////
    private fun handleSavingInputData() {
        saveInputData()
        showToast()
    }

    private fun saveInputData() {
        val inputData = getPreferences(Context.MODE_PRIVATE)
        val tempSavePlace = inputData.edit()
        tempSavePlace.putString("Mass", getMassString())
        tempSavePlace.putString("Height", getHeightString())
        tempSavePlace.putInt("Spinner", spinner.selectedItemPosition)
        tempSavePlace.apply()
    }

    private fun showToast() {
        val text = getString(R.string.saved)
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    private fun findShareActionProvider(menu: Menu) {
        val shareItem = menu.findItem(R.id.share)
        shareActionProvide = MenuItemCompat.getActionProvider(shareItem) as ShareActionProvider
    }

    private fun chooseColor(bmi: Float): Int {
        if (bmi > 30f || bmi < 16)
            return Color.RED
        else if (bmi < 30f && bmi > 25f || bmi < 18.5 && bmi > 16.99f)
            return Color.CYAN
        else if (bmi > 18.5f && bmi < 25f)
            return Color.GREEN
        else
            return Color.BLACK
    }



    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        showShareItemIfBmiIsCounted(menu)
        prepareSharingIntent()
        return true
    }

    private fun showShareItemIfBmiIsCounted(menu: Menu) {
        menu.findItem(R.id.share).isVisible = tvBmiResult.text.isNotEmpty()
    }

    private fun prepareSharingIntent() {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TEXT, getSharingMessage())
        sendIntent.type = "text/plain"
        shareActionProvide.setShareIntent(sendIntent)
    }

    private fun getSharingMessage(): String {
        val sharingMessageTemplate = getString(R.string.shareMsg)
        return String.format(sharingMessageTemplate +" "+ tvBmiResult.text)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.author -> {
                startAuthorActivity()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startAuthorActivity() {
        val intent = Intent(this, AuthorActivity::class.java)
        startActivity(intent)
    }

    private fun setResult(bmi: Float) {
        tvBmiResult.setTextColor(chooseColor(bmi))
        tvBmiResult.text = java.lang.Float.toString(bmi)
    }

    private fun checkInputData() {
        val isMassValid = checkMassInput()
        val isHeightValid = checkHeightInput()
        val isInputValid = isMassValid && isHeightValid
        btnSave.isEnabled = isInputValid
        btnCount.isEnabled = isInputValid
    }


    private fun hideKeyboard(activity: Activity) {
        val inputManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

    private fun checkMassInput(): Boolean {
        return try {
            checkMass()
        } catch (e: NumberFormatException) {
            etMass.error = getString(R.string.NaN)
            false
        }
    }

    private fun checkMass(): Boolean {
        val isValid = counter.isMassValid(getMass())
        if (!isValid) {
            val template = getString(R.string.inputRange)
            etMass.error = String.format(template, counter.minMass, counter.maxMass)
        }
        return isValid
    }

    private fun checkHeightInput(): Boolean {
        return try {
            checkHeight()
        } catch (e: NumberFormatException) {
            etHeight.error = getString(R.string.NaN)
            false
        }
    }

    private fun checkHeight(): Boolean {
        val isValid = counter.isHeighValid(getHeight())
        if (!isValid) {
            val template = getString(R.string.inputRange)
            etHeight.error = String.format(template, counter.minHeigh, counter.maxHeigh)
        }
        return isValid
    }

    private fun inputIsGood(): Boolean {
        if (etMass.text.toString() == "" || etHeight.text.toString() == "")
            return false
        else
            return true
    }

    private fun getMass() = getMassString().toFloat()
    private fun getMassString() = etMass.text.toString()
    private fun getHeight() = getHeightString().toFloat()
    private fun getHeightString() = etHeight.text.toString()

}






