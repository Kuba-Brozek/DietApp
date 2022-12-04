package ayathe.project.dietapp.registerlogin.register.userDataInput.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import ayathe.project.dietapp.R
import ayathe.project.dietapp.registerlogin.activityreglog.LogRegActivity
import ayathe.project.dietapp.registerlogin.login.LoginFragment
import ayathe.project.dietapp.registerlogin.register.RegisterFragment
import com.github.barteksc.pdfviewer.PDFView


class PrivacyPolicyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.privacy_policy_fragment, container, false)

        val pdfViewer: PDFView = view.findViewById(R.id.pdf_viewer)
        pdfViewer.fromAsset("mobile_policy.pdf").load()

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = RegisterFragment()
                (activity as LogRegActivity).fragmentsReplacements(fragment)
            }
        })

        return view
    }
}