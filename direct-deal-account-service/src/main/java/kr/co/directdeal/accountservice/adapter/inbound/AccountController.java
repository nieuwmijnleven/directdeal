package kr.co.directdeal.accountservice.adapter.inbound;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import kr.co.directdeal.accountservice.service.AccountService;
import kr.co.directdeal.accountservice.service.dto.AccountDTO;
import kr.co.directdeal.accountservice.service.dto.PasswordDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {
	
	private final AccountService accountService;
	
	@RequestMapping(method = RequestMethod.OPTIONS)
	public ResponseEntity<?> options() {
		return ResponseEntity
				.ok()
				.allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, 
						HttpMethod.DELETE, HttpMethod.OPTIONS)
				.build();
	}
	
	@PostMapping
	public ResponseEntity<?> createAccount(@Valid @RequestBody AccountDTO accountDTO) {
		AccountDTO newAccountDTO = accountService.createAccount(accountDTO);
		URI selfLink = ServletUriComponentsBuilder
						.fromCurrentRequest()
						.path("/{id}")
        				.buildAndExpand(newAccountDTO.getId())
						.toUri();

		return ResponseEntity.created(selfLink).build();

		// * HATEOAS Support
		// EntityModel<AccountDTO> entityModel = EntityModel.of(newAccountDTO, 
		// 	linkTo(methodOn(AccountController.class).getAccount(newAccountDTO.getId())).withSelfRel(),
		// 	linkTo(methodOn(AccountController.class).updateAccount(newAccountDTO.getId(), newAccountDTO)).withRel("update"),
		// 	linkTo(methodOn(AccountController.class).deleteAccount(newAccountDTO.getId())).withRel("delete"));
		// return ResponseEntity
		// 		.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
		// 		.body(entityModel);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateAccount(@PathVariable("id") String id, @Valid @RequestBody AccountDTO accountDTO) {
		accountDTO.setId(id);
		accountService.updateAccount(accountDTO);
		URI selfLink = ServletUriComponentsBuilder
						.fromCurrentRequest()
						.build()
						.toUri();

		return ResponseEntity.created(selfLink).build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getAccount(@PathVariable("id") String id) {
		var accountDTO = accountService.getAccount(AccountDTO.builder().id(id).build());
		return ResponseEntity.ok(accountDTO);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAccount(@PathVariable("id") String id) {
		accountService.deleteAccount(AccountDTO.builder().id(id).build());
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/change-password")
	public ResponseEntity<?> changePassword(@PathVariable("id") String id, @Valid @RequestBody PasswordDTO passwordDTO) {	
		passwordDTO.setId(id);
		accountService.changePassword(passwordDTO);
		return ResponseEntity.ok().build();
	}
}
