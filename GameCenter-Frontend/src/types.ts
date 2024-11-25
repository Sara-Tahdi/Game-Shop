interface GameCategory {
  id: number;
  category: string;
}

interface Game {
  id: number;
  title: string;
  price: number;
  description: string;
  rating: number;
  remainingQuantity: number;
  isOffered: boolean;
  publicOpinion: string;
  category: GameCategory;
}

interface Review {
  id: number;
  author: string;
  reviewMessage: string;
  managerReply?: string;
  rating: Rating;
  game: Game;
}

type Rating = "FIVE" | "FOUR" | "THREE" | "TWO" | "ONE";
