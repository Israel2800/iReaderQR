package com.israelaguilar.ireaderqr

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.MalformedJsonException
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.media3.common.util.Log
import androidx.navigation.fragment.findNavController
import com.israelaguilar.ireaderqr.databinding.FragmentScannerBinding
import java.net.URL


class ScannerFragment : Fragment() {

    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Método para leer los códigos QR
        binding.cbvScanner.decodeContinuous { result ->
            Toast.makeText(
                requireContext(),
                "Código leído: ${result.text}",
                Toast.LENGTH_SHORT
            ).show()

            Log.d("APPLOGS", "Código leído ${result.text}")

            try {

                URL(result.text)

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(result.text)
                startActivity(intent)

            }catch (e: MalformedJsonException){
                AlertDialog.Builder(requireContext())
                    .setTitle("Error")
                    .setMessage("El código QR no es válido para la aplicación")
                    .setNeutralButton("Aceptar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setIcon(android.R.drawable.ic_delete)
                    .create()
                    .show()
            }

            binding.cbvScanner.pause()
            findNavController().navigate(R.id.action_scannerFragment_to_mainFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.cbvScanner.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.cbvScanner.pause()
    }


}