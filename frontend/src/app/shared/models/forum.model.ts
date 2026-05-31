export interface PostDTO {
  id?: number;
  title: string;
  content: string;
  createdAt?: string;
  user?: { idUser: number; nom?: string; prenom?: string; };
  tags?: string[];
  commentCount?: number;
  trendScore?: number;
}

export interface CommentDTO {
  id?: number;
  content: string;
  createdAt?: string;
  postId: number;
  user?: { idUser: number; nom?: string; prenom?: string; };
}
