package com.example.helpdesk.comment;

import com.example.helpdesk.HelpdeskLogger;
import com.example.helpdesk.ticket.Ticket;
import com.example.helpdesk.ticket.TicketRepository;
import com.example.helpdesk.user.Role;
import com.example.helpdesk.user.User;
import com.example.helpdesk.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private static final List<String> ALLOWED_CONTENT_TYPES= Arrays.asList(
            "image/jpeg",
            "image/png",
            "application/java-archive",
            "application/x-elf"
    );

    public int addCommentToTicket(Optional<MultipartFile> file, String comment, Long ticketId) throws IOException {
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if(ticketOptional.isEmpty()){
            return 1;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return 4;
        }

        var user = (User) authentication.getPrincipal();
        if(user == null){
            return 4;
        }

        Comment commentToSave = null;
        if(file.isPresent()) {
            commentToSave = Comment.builder()
                    .ticket(ticketOptional.get())
                    .userCreator(user)
                    .file(file.get())
                    .comment(comment)
                    .build();
        }else{
            commentToSave = Comment.builder()
                    .ticket(ticketOptional.get())
                    .userCreator(user)
                    .comment(comment)
                    .build();
        }

        Ticket ticket = ticketOptional.get();
        if(ticket.getUserAssignee()!=null){
            if(ticket.getUserAssignee().getUsername().equals(user.getUsername())){
                commentRepository.save(commentToSave);
            }
        }

        if(ticket.getUserCreator()!=null){
            if(ticket.getUserCreator().getUsername().equals(user.getUsername())){
                commentRepository.save(commentToSave);
            }
        }

        if(user.getRole().equals(Role.ADMIN)){
            commentRepository.save(commentToSave);
        }

        if(file.isPresent()) {
            HelpdeskLogger.logFile(file.get());
        }
        return 0;
    }

    public Optional<Comment> getComment(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment;
    }
}
