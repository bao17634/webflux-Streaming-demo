package io.byr.streaming.portfolio;

import reactor.core.publisher.Flux;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;

@Controller
public class HomeController {

    private final UserRepository userRepository;

    private final Flux<Quote> quoteStream;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.quoteStream = WebClient.create("http://localhost:8081")
                .get()
                .uri("/quotes")
                // 设置数据接收类型
                .accept(APPLICATION_STREAM_JSON)
                .retrieve()
                .bodyToFlux(Quote.class)
                //返回新的Flux
                .share()
                .log();
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("users", this.userRepository.findAll());
        return "index";
    }

    @GetMapping(path = "/quotes/feed", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<Quote> fetchQuotesStream() {
        try {
            Flux<Quote> quoteFlux = this.quoteStream;
            Quote quote = new Quote();
            quote.getInstant();
            System.out.println(this.quoteStream);
            return quoteFlux;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/quotes")
    public String quotes() {
        return "quotes";
    }
}
