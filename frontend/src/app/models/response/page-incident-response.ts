import { IncidentResponse } from './incident-response';
import { Pageable, Sort } from './pageable';

export interface PageIncidentResponse {
  content: IncidentResponse[];
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  pageable: Pageable;
  size: number;
  sort: Sort;
  totalElements: number;
  totalPages: number;
}
