package webLogger.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import webLogger.service.Visitor;
import webLogger.service.VisitorsService;
//TODO идентификация по печенькам?
@Controller
@RequestMapping("/")
public class VController {
    
    public final static int MAX_LENGTH = 255;
    public final static int IP_MAX_LENGTH = 30;
    private List <Visitor> list;
    private Visitor current;

    @Resource(name = "visitorService")
    private VisitorsService vs;

    //@RequestHeader("X-Forwarded-For") String ip,
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(@RequestHeader(required = false, value = "User-Agent") String useragent,
            @RequestHeader(required = false, value = "referer") String referer,
            HttpServletRequest request) {

        useragent = Tools.trim(useragent, MAX_LENGTH);
        //useragent = Tools.reEncode(useragent);
        referer = Tools.trim(referer, MAX_LENGTH);
        
        System.out.println("UA: " + useragent);
        System.out.println("Referer: " + referer);
      
        String ip = getClientIpAddr(request);
        ip =  ip.replaceAll(", 127.0.0.1", "");
        ip = Tools.trim(ip, IP_MAX_LENGTH);
        
        boolean added = vs.add(ip, useragent);
//        System.out.println("added: " + added);

        if (added) {
            list = vs.getAll(); // полный список посетителей 
            current = list.remove(0);   // последний посетитель показывается отдельно от основного списка
        } else {
            String date = vs.get(ip).getDate();   //получение даты не обновлённого текущего посетителя
            vs.editUserAgent(ip, useragent);  //обновляется текущий посетитель
            list = vs.getAll();
            current = list.remove(0);
            current.setDate(date);
        }

//        System.out.println("IP\t DATE\t POST\t");
//        for (Visitor v : list) {
//            System.out.println(v.toString());
//        }

        ModelAndView modelAndView = new ModelAndView("visitors");
        modelAndView.addObject("currentVisitor", current);
        modelAndView.addObject("wasHere", !added);
        modelAndView.addObject("visitorsList", list);
        //modelAndView.addObject("headerList", h);

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(@RequestParam("post") String post, HttpServletRequest request) {
        System.out.println("onSubmit() post: " + post);
        post = Tools.trim(post, MAX_LENGTH);
        post = Tools.antiHakzor(post);
        if (!post.isEmpty())
        vs.editPost(getClientIpAddr(request), post);
        return "redirect:/";
    }
    
    
    public static  String getClientIpAddr(HttpServletRequest request) {
        ArrayList <String> ar  = new ArrayList();
        String ip = request.getHeader("X-Forwarded-For");
        System.out.println("X-Forwarded-For: " + ip);
        ar.add("X-Forwarded-For: " + ip);
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            System.out.println("Proxy-Client-IP: " + ip);
            ar.add("Proxy-Client-IP: " + ip);
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            System.out.println("WL-Proxy-Client-Ip: " + ip);
             ar.add("WL-Proxy-Client-Ip: " + ip);
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            System.out.println("HTTP_CLIENT_IP: " + ip);
             ar.add("HTTP_CLIENT_IP: " + ip);
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            System.out.println("HTTP_X_FORWARDED_FOR " + ip);
             ar.add("HTTP_X_FORWARDED_FOR " + ip);
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            System.out.println(".getRemoteAddr(): " + ip);
             ar.add(".getRemoteAddr(): " + ip);
        }
      return ip;
    }
}