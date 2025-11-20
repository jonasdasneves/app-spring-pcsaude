package br.com.pcsaude.controller;

import br.com.pcsaude.entities.Dispositivo;
import br.com.pcsaude.entities.Medicao;
import br.com.pcsaude.mappers.MedicaoMapper;
import br.com.pcsaude.records.MedicaoInDto;
import br.com.pcsaude.records.MedicaoOutDto;
import br.com.pcsaude.services.DispositivoService;
import br.com.pcsaude.services.MedicaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("api/medicoes")
public class MedicaoController {

    private final MedicaoService service;

    private final DispositivoService dispositivoService;

    public MedicaoController(MedicaoService service, DispositivoService dispositivoService) {
        this.service = service;
        this.dispositivoService = dispositivoService;
    }

    @Operation(summary = "Registrar medição", description = "Registra uma nova medição vinda de um dispositivo (UUID)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medição registrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<MedicaoOutDto> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados da medição", required = true) @Valid @RequestBody MedicaoInDto dto) {

        Dispositivo dispositivo = new Dispositivo(dto.uuidDispositivo());

        Medicao medicao = MedicaoMapper.fromDto(dispositivo, dto);

        this.dispositivoService.save(dispositivo);

        MedicaoOutDto medicaoSalva =  MedicaoMapper.toDto(this.service.save(medicao));

        return ResponseEntity.ok(medicaoSalva);
    }

    @Operation(summary = "Última medição", description = "Retorna a última medição registrada")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Última medição retornada"),
            @ApiResponse(responseCode = "204", description = "Sem conteúdo")
    })
    @GetMapping("/ultima-medicao")
    public ResponseEntity<MedicaoOutDto> findUltimaMedicao() {
        MedicaoOutDto ultimaMedicao = MedicaoMapper.toDto(this.service.findUltimaMedicao());
        return ResponseEntity.ok(ultimaMedicao);
    }

    @Operation(summary = "Listar medições", description = "Retorna uma página com medições")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página retornada"),
            @ApiResponse(responseCode = "204", description = "Sem conteúdo")
    })
    @GetMapping
    public ResponseEntity<Page<MedicaoOutDto>> findAll(
            @Parameter(description = "Página (0-based)") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(required = false, defaultValue = "10") int size) {

        Page<MedicaoOutDto> pagina = this.service
                                            .findAll(page, size)
                                            .map(MedicaoMapper::toDto);

        if (pagina.getTotalElements() > 0) {
            return ResponseEntity.ok(pagina);
        }
        else{
            return ResponseEntity.noContent().build();
        }
    }
}
