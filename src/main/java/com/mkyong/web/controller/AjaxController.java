package com.mkyong.web.controller;

import java.util.Arrays;
import java.util.List;


import com.mkyong.web.service.WaterFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.mkyong.web.jsonview.Views;
import com.mkyong.web.model.AjaxResponseBody;
import com.mkyong.web.model.SearchCriteria;
import com.mkyong.web.service.WaterFiller;

@RestController
public class AjaxController {

	@Autowired
	WaterFiller waterFiller;
	// @ResponseBody, not necessary, since class is annotated with @RestController
	// @RequestBody - Convert the json data into object (SearchCriteria) mapped by field name.
	// @JsonView(Views.Public.class) - Optional, limited the json data display to client.
//	@JsonView(Views.Public.class)
	@RequestMapping(value = "/search/api/getSearchResult")
	public AjaxResponseBody getSearchResultViaAjax(@RequestBody SearchCriteria search) {

		AjaxResponseBody result = new AjaxResponseBody();

		List<Integer> list = search.getData();

		//AjaxResponseBody will be converted into json format and send back to client.
//		List<Integer> data = Arrays.asList(0, 0, 0, 0, 0, 5, 5, 0, 8, 8,
//				0, 10, 10, 10, 10, 10, 10, 10, 0, 0);

		List<Integer> data = waterFiller.Compute(list);

		result.setCode("200");
		result.setMsg("");
		result.setResult(data);

		return result;
	}

}
