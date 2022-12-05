package com.hanssem.app.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.hanssem.app.dto.*;
import com.hanssem.app.service.SearchService;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    SearchService searchService;
    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public String search(SearchCondition sc, Model m) {

        try {
            Integer page = sc.getPage();
            Integer pageSize = sc.getPageSize();
            String sort = sc.getSort();
            Integer category = sc.getCategory();
            Integer size = sc.getSize();
            Integer color = sc.getColor();
            Integer minPrice = sc.getMinPrice();
            Integer maxPrice = sc.getMaxPrice();
            if(page==null){
                page=1;
                sc.setPage(1);
            }
            if(page<1){
                page=1;
                sc.setPage(1);
            }
            if(pageSize==null) {
                pageSize = 20;
                sc.setPageSize(20);
            }
            if(pageSize>60){
                pageSize = 60;
                sc.setPageSize(60);
            }
            if(sort==null){
                sort = "popular";
                sc.setSort("popular");
            }

            List<SizeFilterDto> cateSize = null;
            List<ColorFilterDto> cateColor = null;
            List<CateFilterDto> cateList = searchService.getCateInfo(sc);
            Integer totalCount = searchService.getSearchResultCount(sc);
            if(category!=null){
                for (CateFilterDto cfd: cateList) {
                    if(cfd.getCategory_code_number().equals(category)){
                        totalCount = cfd.getCategory_amount();
                    }
                }
                cateSize = searchService.getSizeList(sc);
                cateColor = searchService.getColorList(sc);
            }
            if(size!=null){
                for(SizeFilterDto sfd : cateSize) {
                    if(sfd.getSize_code_number().equals(size)){
                        totalCount = sfd.getSize_amount();
                    }
                }
            }
            if(color!=null){
                for(ColorFilterDto cfd : cateColor) {
                    if(cfd.getColor_code_number().equals(color)){
                        totalCount = cfd.getColor_amount();
                    }
                }
            }
            if(minPrice!=null||maxPrice!=null){
                totalCount = searchService.getPriceAmount(sc);
                System.out.println("totalcount : " + totalCount);
            }
            PageHandler ph = new PageHandler(totalCount, page, pageSize);
            System.out.println(sc);
            sc.setOffset((page-1)*pageSize);
            sc.setPageSize(pageSize);
            sc.setSort(sort);
            Integer totalProduct = searchService.getSearchResultCount(sc);
            List<SearchResult> list = searchService.getSearchResultList(sc);
            for (SearchResult sr:list
                 ) {
                System.out.println(sr);
            }

            if(cateSize!=null){
                m.addAttribute("cateSize", cateSize);
                m.addAttribute("cateColor", cateColor);
            }

            m.addAttribute("totalAmount", totalProduct);
            m.addAttribute("sc", sc);
            m.addAttribute("cate", cateList);
            m.addAttribute("list", list);
            m.addAttribute("ph", ph);
            m.addAttribute("page", page);
            m.addAttribute("pageSize", pageSize);

            return "search";
        } catch (Exception e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
            return "redirect:/";
        }
    }

}
