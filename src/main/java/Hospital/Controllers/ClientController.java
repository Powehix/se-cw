package Hospital.Controllers;

import Hospital.Model.Client;
import Hospital.Model.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class ClientController {

	@Autowired
	private ClientRepository clientRepository;

	@GetMapping("/client")
	public String getEntityPage() {
		return "client";
	}

	@PostMapping("/client/form")
	public @ResponseBody String loadEntityEditFormWithData(Integer id, Boolean adding) {
		if (adding) {
			return clientGetAddFormHtml();
		}

		Client entity = clientRepository.findById(id).orElse(null);

		if(entity == null) {
			return "Not found!";
		}

		return clientGetEditFormHtml(entity);
	}

	@PostMapping("/client/remove")
	public @ResponseBody String deleteSelectedEntity(Integer id) {
		if (clientRepository.existsById(id)) {
			clientRepository.deleteById(id);
			return "Deleted";
		} else {
			return "Not found!";
		}
	}

	@PostMapping(path="/client/save")
	public @ResponseBody
	String addNewEntity (@ModelAttribute Client newData)
	{
		//----- Saving by converted to object received params -----
		Client savedClient = clientRepository.save(newData);
		return savedClient.getIdClient().toString();
	}

	@PostMapping(path="/client/all")
	public @ResponseBody String getAllClients() {
		ArrayList<Client> allClients = new ArrayList<>();
		clientRepository.findAll().forEach(allClients::add);
		String requestResult = "";
		for (Client oneClient : allClients) {
			requestResult += clientToHtmlBlock(oneClient);
		}

		return requestResult;
	}

	public String clientToHtmlBlock(Client client) {
		return "<div class=\"company-element entity\" onclick=\"openDataForm('"+ client.getIdClient()  +"')\">\n" +
				"<div class=\"company-image-container\">\n" +
				"<img src=\"/images/company.png\" alt=\"Client company image\">\n" +
				"</div>\n" +
				"<div class=\"company-info-container\">\n" +
				"<span>" + client.getFullName() +"</span>\n" +
				"<span>"+ client.getPersonalCode() +"</span>\n" +
				"</div>\n" +
				"</div>";
	}

	public String clientGetEditFormHtml(Client client) {
		return "<div id=\"form-edit-container\" class=\"form-place-holder\">\n" +
				"                    <div class=\"form-container\">\n" +
				"                        <form id=\"edit-entity-form\" action=\"/client/save\" method=\"post\">\n" +
				"               	         <p>Name: <input required type=\"text\" name=\"name\" class=\"data\" value='"+ client.getName() +"' /></p>\n" +
				"           	             <p>Surname: <input required type=\"text\" name=\"surname\" class=\"data\" value='"+ client.getSurname() +"' /></p>\n" +
				"       	                 <p>Personal Code: <input required type=\"text\" name=\"personalCode\" class=\"data\" value='"+ client.getPersonalCode() +"' /></p>\n" +
				"   	                     <p>Phone: <input required type=\"text\" name=\"phone\" class=\"data\" value='" + client.getPhone() + "' /></p>\n" +
				"	                         <p>Email: <input type=\"text\" name=\"email\" class=\"data\" value='" + client.getEmail() + "' /></p>\n" +
				"                            <input type=\"hidden\" name=\"idClient\" class=\"data\" value='"+ client.getIdClient() +"'/>"+
				"                        </form>\n" +
				"                        <div class=\"form-navigation\">\n" +
				"                            <a onclick=\"submitDataForm('edit-entity-form')\" class=\"button-a\">\n" +
				"                                <img src=\"/images/save.png\" class=\"form-menu-image\">\n" +
				"                                <span>UPDATE</span>\n" +
				"                            </a>\n" +
				"                            <a onclick=\"entityRemoving('"+ client.getIdClient() +"')\" class=\"button-a\">\n" +
				"                                <img src=\"/images/delete.png\" class=\"form-menu-image\">\n" +
				"                                <span>REMOVE</span>\n" +
				"                            </a>\n" +
				"                        </div>\n" +
				"                        <a onclick=\"hideForm('edit-entity-form');\" class=\"button-a form-cancel\">\n" +
				"                            <img src=\"/images/cancel.png\" class=\"form-menu-image\">\n" +
				"                            <span>CANCEL</span>\n" +
				"                        </a>\n" +
				"                    </div>\n" +
				"                </div>";
	}

	public String clientGetAddFormHtml() {
		return "<div id=\"form-add-container\" class=\"form-place-holder\">\n" +
				"                <div class=\"form-container\">\n" +
				"                    <form id=\"add-entity-form\" action=\"/client/save\" method=\"post\">\n" +
				"                        <p>Name: <input required type=\"text\" name=\"name\" class=\"data\"/></p>\n" +
				"                        <p>Surname: <input required type=\"text\" name=\"surname\" class=\"data\"/></p>\n" +
				"                        <p>Personal Code: <input required type=\"text\"name=\"personalCode\" class=\"data\"/></p>\n" +
				"                        <p>Phone: <input required type=\"text\" name=\"phone\" class=\"data\"/></p>\n" +
				"                        <p>Email: <input type=\"text\" name=\"email\" class=\"data\"/></p>\n" +
				"                    </form>\n" +
				"                    <div class=\"form-navigation\">\n" +
				"                        <a onclick=\"submitDataForm('add-entity-form')\" class=\"button-a\">\n" +
				"                            <img src=\"/images/save.png\" class=\"form-menu-image\">\n" +
				"                            <span>SAVE</span>\n" +
				"                        </a>\n" +
				"                        <a onclick=\"resetForm('add-entity-form')\" class=\"button-a\">\n" +
				"                            <img src=\"/images/repeat.png\" class=\"form-menu-image\">\n" +
				"                            <span>RESET</span>\n" +
				"                        </a>\n" +
				"                    </div>\n" +
				"                    <a onclick=\"hideForm('add-entity-form');\" class=\"button-a form-cancel\">\n" +
				"                        <img src=\"/images/cancel.png\" class=\"form-menu-image\">\n" +
				"                        <span>CANCEL</span>\n" +
				"                    </a>\n" +
				"                </div>\n" +
				"            </div>";
	}
}
