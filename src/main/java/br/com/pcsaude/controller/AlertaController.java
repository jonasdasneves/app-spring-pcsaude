package br.com.pcsaude.controller;

import br.com.pcsaude.entities.Alerta;
import br.com.pcsaude.mappers.AlertaMapper;
import br.com.pcsaude.records.AlertaOutDto;
import br.com.pcsaude.services.AlertaService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("api/alertas")
public class AlertaController {

    private final AlertaService service;

    public AlertaController(AlertaService service) {
        this.service = service;
    }

    @Operation(summary = "Buscar alerta por id", description = "Retorna um alerta pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alerta encontrado"),
            @ApiResponse(responseCode = "404", description = "Alerta não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AlertaOutDto> findById(@Parameter(description = "Id do alerta", required = true) @PathVariable Long id) {
        Alerta alerta = service.findById(id);
        return ResponseEntity.ok(AlertaMapper.toDto(alerta));
    }

    @Operation(summary = "Listar alertas do usuário", description = "Retorna uma página com os alertas do usuário autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página retornada"),
            @ApiResponse(responseCode = "204", description = "Sem conteúdo")
    })
    @GetMapping
    public ResponseEntity<Page<AlertaOutDto>> findAllByUsuario(
            @Parameter(description = "Página (0-based)") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(required = false, defaultValue = "10") int size) {
        Page<AlertaOutDto> pagina = this.service
                                        .findAll(page, size)
                                        .map(AlertaMapper::toDto);

        if (pagina.getTotalElements() > 0) {
            return ResponseEntity.ok(pagina);
        }
        else{
            return ResponseEntity.noContent().build();
        }
    }
}
