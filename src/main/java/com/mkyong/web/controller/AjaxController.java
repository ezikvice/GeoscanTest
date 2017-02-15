package com.mkyong.web.controller;

import java.util.List;

import com.mkyong.web.service.WaterFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import com.mkyong.web.model.AjaxResponseBody;
import com.mkyong.web.model.SearchCriteria;

import javax.servlet.http.HttpServletRequest;

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

        List<Integer> data = waterFiller.Compute(list);

        result.setCode("200");
        result.setMsg("");
        result.setResult(data);

        return result;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    AjaxResponseBody
    handleBadRequest(HttpServletRequest req, Exception ex) {
        AjaxResponseBody result = new AjaxResponseBody();

        result.setCode("400");
        result.setMsg(ex.getLocalizedMessage());
        result.setResult(null);

        return result;
    }

}
