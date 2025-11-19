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

    @GetMapping("/{id}")
    public ResponseEntity<SuporteOutDto> findById(@PathVariable Long id) {
        Suporte suporte = this.service.findById(id);
        SuporteOutDto suporteOutDto = SuporteMapper.toDto(suporte);
        return ResponseEntity.ok(suporteOutDto);
    }

    @GetMapping
    public ResponseEntity<Page<SuporteOutDto>> findAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

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

    @PostMapping
    public ResponseEntity<SuporteOutDto> save(@Valid @RequestBody SuporteInDto dto) {

        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Suporte suporte = SuporteMapper.fromDto(usuario, dto);
        Suporte newSuporte = this.service.save(suporte);
        SuporteOutDto novoSuporte = SuporteMapper.toDto(newSuporte);

        return ResponseEntity.ok(novoSuporte);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuporteOutDto> cancelar(@PathVariable Long id) {
        Suporte suporteAtualizado = this.service.cancelar(id);
        SuporteOutDto suporteCancelado = SuporteMapper.toDto(suporteAtualizado);

        return ResponseEntity.ok(suporteCancelado);
    }
}
