package com.tony.quartzbatchtemplate.controller;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.Cookie;

@Controller
@SessionAttributes("orderSessionDTO")
public class OrderController {
   @GetMapping("/orders")
   public String toOrder(Model model, @CookieValue(value="storeIdCookie", required = false) Cookie storeIdCookie) {
      LoginInfo loginInfo = new LoginInfo();
      if (storeIdCookie != null)
      {
         loginInfo.setId(storeIdCookie.getValue());
         loginInfo.setStoreId(true);
      }
      model.addAttribute("orderSessionDTO", "order-page");
      return "orders";
   }

   @Data
   private class LoginInfo {
      private String id;
      private boolean storeId;
   }
}
