package com.demo.DBPBackend.comentario.infrastructure;

import com.demo.DBPBackend.comentario.domain.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
}
