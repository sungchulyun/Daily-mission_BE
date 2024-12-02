package dailymissionproject.demo.domain.notify.repository;

import dailymissionproject.demo.common.repository.BaseTimeEntity;
import dailymissionproject.demo.domain.user.repository.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Notification extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    private String content;

    private boolean checked;

    private boolean deleted;

    @Builder
    public Notification(User receiver, NotificationType notificationType, String content) {
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.content = content;

        this.checked = false;
        this.deleted = false;
    }

    public void confirmNotification(){
        this.checked = true;
    }

    public void delete() {
        this.deleted = true;
    }
}
