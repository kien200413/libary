import { Category } from "./category";

export interface CategoryResponse {
  data: Category[];
  total: number;
  page: number;
  limit: number;
}
