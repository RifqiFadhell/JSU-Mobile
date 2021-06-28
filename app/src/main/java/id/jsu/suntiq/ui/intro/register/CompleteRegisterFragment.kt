package id.jsu.suntiq.ui.intro.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import id.jsu.suntiq.R
import id.jsu.suntiq.api.request.register.RegisterRequest
import id.jsu.suntiq.api.response.register.RegisterResponse
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_FORM
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_REGISTER
import id.jsu.suntiq.preference.tinyDb.TinyDB
import id.jsu.suntiq.ui.intro.register.verify.VerifyRegisterActivity
import id.jsu.suntiq.utils.LoaderIndicatorHelper
import id.jsu.suntiq.utils.base.BaseFragment
import id.jsu.suntiq.utils.extensions.goToActivity
import id.jsu.suntiq.utils.extensions.showOkDialog
import kotlinx.android.synthetic.main.register_complete_fragment.*
import java.util.*

class CompleteRegisterFragment : BaseFragment(), RegisterContract.View {

    private var nameBank = listOf("BCA", "BRI", "BNI", "MANDIRI", "BTN", "BJB", "Lainnya")
    private var selectedNameBank = ""
    private var date = ""
    private var presenter: RegisterPresenter? = null

    override fun provideLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.register_complete_fragment, container, false)
    }

    override fun init(bundle: Bundle?) {
        initSpinner()
        presenter = RegisterPresenter(this)
    }

    override fun initData(bundle: Bundle?) {

    }

    override fun initListener(bundle: Bundle?) {
        buttonNext.setOnClickListener {
            validateFinal()
        }

        editNameBank.setOnClickListener {
            spinnerDestinationListener()
            spinnerNameBank.performClick()
        }

        editDate.setOnClickListener {
            getDate()
        }
    }

    private fun initSpinner() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nameBank)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinnerNameBank.adapter = adapter
    }

    private fun getDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dialog = DatePickerDialog(
            requireContext(), { _, years, monthOfYear, dayOfMonth ->
                val months = monthOfYear + 1

                val days: String = if (dayOfMonth < 10) {
                    "0$dayOfMonth"
                } else {
                    dayOfMonth.toString()
                }

                val finalMonth: String = if (months < 10) {
                    "0$months"
                } else {
                    months.toString()
                }

                val finalDate = "$days-$finalMonth-$years"
                date = "$years-$finalMonth-$days"
                editDate.setText(finalDate)
            },
            year,
            month,
            day
        )
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -16)
        dialog.datePicker.maxDate = cal.timeInMillis
        dialog.show()
    }


    private fun spinnerDestinationListener() {
        spinnerNameBank.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedNameBank = nameBank[position]
                    editNameBank.setText(nameBank[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun validateFinal() {
        val numberId = editNumberId.editableText.toString()
        val locationBirth = editLocationBirth.editableText.toString()
        val dateBirth = editDate.editableText.toString()
        val numberBank = editNumberBank.editableText.toString()
        val nameBank = editNameBank.editableText.toString()
        val saved = TinyDB(requireContext()).getObject(TINY_FORM, RegisterRequest::class.java)

        if (locationBirth.isEmpty()) {
            textInputLocation.error = "Tempat lahir tidak boleh kosong"
        } else {
            textInputLocation.error = null
        }

        if (dateBirth.isEmpty()) {
            textInputDate.error = "Tanggal Lahir tidak boleh kosong"
        } else {
            textInputDate.error = null
        }

        if (numberId.isEmpty()) {
            textInputNumberId.error = "NIK tidak boleh kosong"
        } else {
            textInputNumberId.error = null
        }

        if (numberBank.isEmpty()) {
            textInputNumberBank.error = "Nomor Rekening tidak boleh kosong"
        } else {
            textInputNumberBank.error = null
        }

        if (nameBank.isEmpty()) {
            textInputNameBank.error = "Nama Bank tidak boleh kosong"
        } else {
            textInputNameBank.error = null
        }

        if (numberId.isNotEmpty() && date.isNotEmpty() && locationBirth.isNotEmpty() && numberBank.isNotEmpty() && nameBank.isNotEmpty()) {
            val request = RegisterRequest(saved?.phone_number, saved?.password, saved?.fullname, numberId, locationBirth, date, nameBank, numberBank, saved?.imei)
            presenter?.register(request)
            showLoading()
        }
    }

    override fun showLoading() {
        LoaderIndicatorHelper.getInstance().showDialog(requireContext())
    }

    override fun hideLoading() {
        LoaderIndicatorHelper.getInstance().dismissDialog()
    }

    override fun getRegister(response: RegisterResponse) {
        if (response.status == 200) {
            TinyDB(requireContext()).putObject(TINY_REGISTER, response.data)
            requireActivity().goToActivity(VerifyRegisterActivity::class.java)
            requireActivity().finish()
        }
    }

    override fun showError(error: Throwable) {
        requireContext().showOkDialog(error.message.toString(), "Oke", null)
    }
}