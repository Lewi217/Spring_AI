package Wave.Chatgpt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@CrossOrigin
public class ChatGptController {

    private static final Logger log = LoggerFactory.getLogger(ChatGptController.class);
    private final ChatClient chatClient;

    public ChatGptController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
    }

    @GetMapping("")
    public String home() {
        return "index";
    }

    @PostMapping("/api/chat")
    public ModelAndView generate(@RequestParam String message, Model model) {
        log.info("User Message: {}", message);

        // Calling the chat client to get response
        String response = chatClient.prompt()
                .user(message)
                .call()
                .content();

        // Adding attributes to the model
        model.addAttribute("response", response);
        model.addAttribute("message", message);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("response");
        mav.addObject("response", response);

        return mav;
    }

}
