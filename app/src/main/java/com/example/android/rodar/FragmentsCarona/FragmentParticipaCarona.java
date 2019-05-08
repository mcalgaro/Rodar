package com.example.android.rodar.FragmentsCarona;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.rodar.R;
import com.example.android.rodar.Utils.PreferenceUtils;
import com.example.android.rodar.Utils.RetrofitClient;
import com.example.android.rodar.services.CaronaService;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class FragmentParticipaCarona extends Fragment {

    private TextView endereco, mensagem, vagas, valor;
    private Button btnConcluir;
    private int idEventoCarona;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_participa_carona, container, false);

        idEventoCarona = getArguments().getInt("idEventoCarona");

        endereco = v.findViewById(R.id.participa_carona_endereco);
        mensagem = v.findViewById(R.id.participa_carona_mensagem);
        vagas = v.findViewById(R.id.participa_carona_vagas);
        valor = v.findViewById(R.id.participa_carona_valor);
        btnConcluir = v.findViewById(R.id.participa_carona_concluir);
        btnConcluir.setOnClickListener(concluirListener);

        return v;
    }

    View.OnClickListener concluirListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CaronaService service = RetrofitClient.getClient().create(CaronaService.class);
            Call<ResponseBody> call = service.participarCarona(PreferenceUtils.getToken(getContext()), idEventoCarona);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "CARONA CONFIRMADA", Toast.LENGTH_LONG).show();
                        getFragmentManager().popBackStackImmediate();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }
    };


}