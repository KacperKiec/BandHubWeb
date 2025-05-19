export interface Page<T> {
  content: T[];
  page: PageInfo
}

export type PageInfo = {
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}
