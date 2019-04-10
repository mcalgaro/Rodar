package com.example.android.rodar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.android.rodar.activities.ILoginActivity;
import com.example.android.rodar.models.Usuario;
import com.example.android.rodar.services.UsuarioService;

import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class FragmentCadastro extends Fragment {

    private Button btnConclui;
    private TextInputLayout nome, sobrenome, cpf, celular, email, senha, senhaConfirma;
    private RadioGroup genero;
    private RadioButton generoM,generoF;
    private ILoginActivity loginActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cria_cadastro, container, false);

        btnConclui = v.findViewById(R.id.cadastro_concluir);
        btnConclui.setOnClickListener(dataListener);

        nome = v.findViewById(R.id.cadastro_nome);
        sobrenome = v.findViewById(R.id.cadastro_sobrenome);
        genero = v.findViewById(R.id.cadastro_genero);
        generoM = v.findViewById(R.id.cadastro_genero_masculino);
        generoF = v.findViewById(R.id.cadastro_genero_feminino);
        cpf = v.findViewById(R.id.cadastro_cpf);
        celular = v.findViewById(R.id.cadastro_celular);
        email = v.findViewById(R.id.cadastro_email);
        senha = v.findViewById(R.id.cadastro_senha);
        senhaConfirma = v.findViewById(R.id.cadastro_senha2);



        // Inicia como masculino
        generoM.toggle();
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loginActivity = (ILoginActivity) getActivity();
    }


    private View.OnClickListener concluiListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (validaCampos()){
                Usuario novoUsuario = new Usuario();
                novoUsuario.setNome(nome.getEditText().getText().toString());
                novoUsuario.setSobrenome(sobrenome.getEditText().getText().toString());
                if (generoM.isChecked()) {
                    novoUsuario.setGenero("M");
                } else {
                    novoUsuario.setGenero("F");
                }
                novoUsuario.setCPF(cpf.getEditText().getText().toString());
                novoUsuario.setNumeroTelefone(celular.getEditText().getText().toString());
                novoUsuario.setEmail(email.getEditText().getText().toString());
                novoUsuario.setSenha(senha.getEditText().getText().toString());

                registraUsuario(novoUsuario);
            }
        }
    };


    private View.OnClickListener dataListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            showDatePicker();
        }
    };


    private boolean validaCampos(){
      boolean ok = true;

      nome.setError(null);
      sobrenome.setError(null);
      cpf.setError(null);
      celular.setError(null);
      email.setError(null);
      senha.setError(null);
      senhaConfirma.setError(null);

      if (nome.getEditText().getText().toString().isEmpty()) {
          nome.setError("Preencher nome");
          ok = false;
      }
      if (sobrenome.getEditText().getText().toString().isEmpty()){
          sobrenome.setError("Preencher sobrenome");
          ok = false;
      }
      if (cpf.getEditText().getText().toString().isEmpty()){
          cpf.setError("Campo obrigatório");
          ok = false;
      }
      if (celular.getEditText().getText().toString().isEmpty()) {
          celular.setError("Campo obrigatório");
          ok = false;
      }
      if (email.getEditText().getText().toString().isEmpty()){
          email.setError("Campo obrigatório");
          ok = false;
      }
      if (senha.getEditText().getText().toString().isEmpty()){
          senha.setError("Informe a senha");
          ok = false;
      }
      if (senhaConfirma.getEditText().getText().toString().isEmpty()) {
          senhaConfirma.setError("Confirme a senha");
          ok = false;
      }
      if (!senha.getEditText().getText().toString().equals(senhaConfirma.getEditText().getText().toString())) {
          senha.setError("As senhas são diferentes");
          senhaConfirma.setError("As senhas são diferentes");
          ok = false;
      }

      return ok;
    }

    private void registraUsuario(Usuario novoUsuario){
        UsuarioService usrService = RetrofitClient.getClient().create(UsuarioService.class);
        Call<ResponseBody> call = usrService.createUser(novoUsuario);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getContext(), "Cadastrado com sucesso", Toast.LENGTH_LONG).show();
                PreferenceUtils.saveEmail(email.getEditText().getText().toString(), getContext());
                PreferenceUtils.savePassword(senha.getEditText().getText().toString(),getContext());
                loginActivity.loginUsuario();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Erro ao se conectar no servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            Toast.makeText(getContext(),(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year)) , Toast.LENGTH_LONG).show();
        }
    };




}
