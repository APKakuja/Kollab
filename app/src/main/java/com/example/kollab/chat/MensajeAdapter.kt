package com.example.kollab.chat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kollab.R

class MensajeAdapter(
    private var mensajes: List<Mensaje>
) : RecyclerView.Adapter<MessageViewHolder>() {

    companion object {
        private const val TIPO_MIO = 1
        private const val TIPO_OTRO = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (mensajes[position].enviadoPorMi) TIPO_MIO else TIPO_OTRO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layout = when (viewType) {
            TIPO_MIO -> R.layout.item_mensaje_mio
            else -> R.layout.item_mensaje_otro
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val mensaje = mensajes[position]
        holder.texto.text = mensaje.texto
    }

    override fun getItemCount(): Int = mensajes.size

    fun actualizarLista(nuevaLista: List<Mensaje>) {
        mensajes = nuevaLista
        notifyDataSetChanged()
    }
}