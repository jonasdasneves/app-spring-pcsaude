package br.com.pcsaude.controller;

import br.com.pcsaude.entities.Dispositivo;
import br.com.pcsaude.entities.Suporte;
import br.com.pcsaude.entities.Usuario;
import br.com.pcsaude.mappers.MedicaoMapper;
import br.com.pcsaude.mappers.SuporteMapper;
import br.com.pcsaude.records.SuporteInDto;
import br.com.pcsaude.records.SuporteOutDto;
import br.com.pcsaude.security.CustomUserDetails;
import br.com.pcsaude.services.SuporteService;
import br.com.pcsaude.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/suportes")
public class SuporteController {

    private final SuporteService service;

    public SuporteController(SuporteService service) {
        this.service = service;
    }

    @Operation(summary = "Buscar suporte por id", description = "Retorna um pedido de suporte pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Suporte encontrado"),
            @ApiResponse(responseCode = "404", description = "Suporte não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SuporteOutDto> findById(@Parameter(description = "Id do suporte", required = true) @PathVariable Long id) {
        Suporte suporte = this.service.findById(id);
        SuporteOutDto suporteOutDto = SuporteMapper.toDto(suporte);
        return ResponseEntity.ok(suporteOutDto);
    }

    @Operation(summary = "Listar suportes do usuário", description = "Retorna uma página com os suportes do usuário autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página retornada"),
            @ApiResponse(responseCode = "204", description = "Sem conteúdo")
    })
    @GetMapping
    public ResponseEntity<Page<SuporteOutDto>> findAll(
            @Parameter(description = "Página (0-based)") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(required = false, defaultValue = "10") int size) {

        Page<SuporteOutDto> pagina = this.service
                .findAll(page, size)
                .map(SuporteMapper::toDto);

        if (pagina.getTotalElements() > 0) {
            return ResponseEntity.ok(pagina);
        }
        else{
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "Criar solicitação de suporte", description = "Cria um novo pedido de suporte para o usuário autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Suporte criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<SuporteOutDto> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do suporte", required = true) @Valid @RequestBody SuporteInDto dto) {

        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Suporte suporte = SuporteMapper.fromDto(usuario, dto);
        Suporte newSuporte = this.service.save(suporte);
        SuporteOutDto novoSuporte = SuporteMapper.toDto(newSuporte);

        return ResponseEntity.ok(novoSuporte);
    }

    @Operation(summary = "Cancelar suporte", description = "Cancela um pedido de suporte pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Suporte cancelado"),
            @ApiResponse(responseCode = "404", description = "Suporte não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SuporteOutDto> cancelar(@Parameter(description = "Id do suporte", required = true) @PathVariable Long id) {
        Suporte suporteAtualizado = this.service.cancelar(id);
        SuporteOutDto suporteCancelado = SuporteMapper.toDto(suporteAtualizado);

        return ResponseEntity.ok(suporteCancelado);
    }
}
