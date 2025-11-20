package br.com.pcsaude.services;

import br.com.pcsaude.entities.Alerta;
import br.com.pcsaude.entities.Dispositivo;
import br.com.pcsaude.entities.Medicao;
import br.com.pcsaude.entities.Usuario;
import br.com.pcsaude.enums.GrauAlertaEnum;
import br.com.pcsaude.enums.PosturaEnum;
import br.com.pcsaude.exceptions.ResourceNotFoundException;
import br.com.pcsaude.repositories.MedicaoRepository;
import br.com.pcsaude.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
public class MedicaoService {

    private final MedicaoRepository repository;

    private final AlertaService alertaService;

    public MedicaoService(MedicaoRepository repository, AlertaService alertaService) {
        this.repository = repository;
        this.alertaService = alertaService;
    }

    public Medicao save(Medicao medicao){

        //Verificação de medidas para criação de alertas se necessário:
        criarAlertas(medicao);

        return repository.save(medicao);
    }

    public Page<Medicao> findAll(int page, int size){

        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Dispositivo dispositivo = usuario.getDispositivo();

        return this.repository
                .findAllByDispositivo_Uuid(
                        dispositivo.getUuid(),
                        Pageable
                        .ofSize(size)
                        .withPage(page));
    }

    public Medicao findUltimaMedicao(){

        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Dispositivo dispositivo = usuario.getDispositivo();

        try {
            return this.repository
                    .findFisrtByDispositivo_UuidOrderByMomentoMedicaoDesc(dispositivo.getUuid())
                    .orElseThrow();
        }
        catch (NoSuchElementException e){
            throw new ResourceNotFoundException("Não foi possível encontrar medições para seu dispositivo. ");
        }

    }

    public void criarAlertas(Medicao medicao){

        //Verifica postura para avisar caso esteja problemática
        if (medicao.getPostura().equals(PosturaEnum.POSTURA_CORCUNDA)
        || (medicao.getPostura().equals(PosturaEnum.POSTURA_DESLIZADA))){

            Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Alerta alerta = new Alerta(
                    usuario,
                    medicao,
                    "Postura problemática!",
                    "Você está com uma postura problemática! Corrija sua postura para trabalhar melhor!",
                    GrauAlertaEnum.ALTO
            );

            this.alertaService.save(alerta);
        }

        //Verifica tempo sentado para avisar que o usuário deve levantar
        if (medicao.getTempoSentadoMin() > 120){

            Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Alerta alerta = new Alerta(
                    usuario,
                    medicao,
                    "Muito tempo sentado!",
                    "Você está a muito tempo sentado! Levante e se movimente um pouco!",
                    GrauAlertaEnum.MEDIO
            );

            this.alertaService.save(alerta);
        }

        //Verificar a iluminação no local de trabalho para gerar um alerta
        if (medicao.getIluminacaoLux().compareTo(BigDecimal.ZERO) < 300){
            Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Alerta alerta = new Alerta(
                    usuario,
                    medicao,
                    "Pouca luz para trabalhar!",
                    "Você está com uma iluminação muito baixa! Isso vai forçar seus olhos. Aumente a luz no seu local de trabalho",
                    GrauAlertaEnum.MEDIO
            );

            this.alertaService.save(alerta);
        }

        //Verifica se temperatura está muito baixa
        if (medicao.getTemperaturaC().compareTo(BigDecimal.ZERO) < 20){
            Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Alerta alerta = new Alerta(
                    usuario,
                    medicao,
                    "Temperatura muito baixa!",
                    "A temperatura do seu trabalho é de " + medicao.getTemperaturaC().toString() + "°C. Procure aumentar a temperatura para algo mais confortável",
                    GrauAlertaEnum.ALTO
            );

            this.alertaService.save(alerta);
        }

        //Verifica se temperatura está muito alta
        if (medicao.getTemperaturaC().compareTo(BigDecimal.ZERO) > 27){
            Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Alerta alerta = new Alerta(
                    usuario,
                    medicao,
                    "Temperatura muito alta!",
                    "A temperatura do seu trabalho é de " + medicao.getTemperaturaC().toString() + "°C. Procure abaixar a temperatura para algo mais confortável. Cuidado com mau estar!",
                    GrauAlertaEnum.CRITICO
            );

            this.alertaService.save(alerta);
        }

        //Verifica altura do monitor para fazer recomendações
        if (medicao.getAlturaTelaCm().compareTo(BigDecimal.ZERO) < 100
        || medicao.getAlturaTelaCm().compareTo(BigDecimal.ZERO) > 130){

            Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Alerta alerta = new Alerta(
                    usuario,
                    medicao,
                    "Altura do monitor incorreta",
                    "Cuidado com a altura do monitor, ele deve ficar na altura dos olhos para você não forçar seu pescoço",
                    GrauAlertaEnum.BAIXO
            );

            this.alertaService.save(alerta);
        }
    }
}
