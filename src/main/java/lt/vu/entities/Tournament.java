package lt.vu.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = "Tournament.findAll", query = "select t from Tournament as t")
})
@Table(name = "TOURNAMENT")
@Getter @Setter
public class Tournament {

    public Tournament() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 100)
    @Column(name = "NAME")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "PLAYER_TOURNAMENT",
            joinColumns = @JoinColumn(name = "TOURNAMENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "PLAYER_ID")
    )
    private List<Player> players = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tournament that = (Tournament) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
