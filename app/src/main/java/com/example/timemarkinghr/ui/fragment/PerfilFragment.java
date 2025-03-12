package com.example.timemarkinghr.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.timemarkinghr.R;
import com.example.timemarkinghr.data.remote.ApiService;
import com.example.timemarkinghr.data.remote.RemoteRepository;
import com.example.timemarkinghr.utils.SessaoManager;

/**
 * A simple {@link Fragment} subclass for displaying user profile.
 */
public class PerfilFragment extends Fragment {

    // Argumentos para passar parâmetros, se necessário
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     */
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recuperar os argumentos, caso existam
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar o layout para este fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);


        // Configurar a imagem de perfil
        ImageView imageView = view.findViewById(R.id.imgPerfil);
        imageView.setImageResource(R.drawable.ic_perfil); // Substituir com sua imagem

        // Configurar os campos de nome e email
        TextView nomeTextView = view.findViewById(R.id.tvNomeUsuario);
        TextView emailTextView = view.findViewById(R.id.tvEmailUsuario);

        //em teste
        String nome = SessaoManager.obterNomeUsuario(getContext());
        String email = SessaoManager.obterEmailUsuario(getContext());

        // Preencher com dados de exemplo (podem vir do banco de dados ou da API)
        nomeTextView.setText(nome);
        emailTextView.setText(email);

        return view;
    }

}
