package Hospital.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePageController {
	@RequestMapping(value={"/", "/hospital"})
	public String getEntityPage() {
		return "hospital";
	}
}