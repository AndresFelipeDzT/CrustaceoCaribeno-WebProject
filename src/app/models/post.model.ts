import { Comment } from './comment.model';

export interface PostReactions {
  likes: number;
  dislikes: number;
}

export interface Post {
  id: number;
  title: string;
  body: string;
  tags: string[];
  reactions: PostReactions | number;
  views?: number;
  userId: number;
  comments?: Comment[];
}

export interface PostResponse {
  posts: Post[];
  total: number;
  skip?: number;
  limit?: number;
}
