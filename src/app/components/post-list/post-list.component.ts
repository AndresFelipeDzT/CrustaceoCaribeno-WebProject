import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Post, PostReactions } from '../../models/post.model';

@Component({
  selector: 'app-post-list',
  imports: [CommonModule],
  templateUrl: './post-list.component.html',
  styleUrl: './post-list.component.css'
})
export class PostListComponent {
  @Input() posts: Post[] = [];

  /**
   * Helper para extraer los likes de forma segura, ya sea que DummyJSON
   * retorne un objeto { likes, dislikes } o un número entero simple.
   */
  getLikes(reactions: PostReactions | number | undefined): number {
    if (!reactions) return 0;
    if (typeof reactions === 'object' && 'likes' in reactions) {
      return reactions.likes;
    }
    return typeof reactions === 'number' ? reactions : 0;
  }

  /**
   * Helper para extraer los dislikes
   */
  getDislikes(reactions: PostReactions | number | undefined): number {
    if (!reactions) return 0;
    if (typeof reactions === 'object' && 'dislikes' in reactions) {
      return reactions.dislikes;
    }
    return 0;
  }
}
