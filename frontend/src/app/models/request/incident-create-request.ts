import { Priority } from '../enums/priority';

export interface IncidentCreateRequest {
  titulo: string;
  descricao?: string;
  prioridade: Priority;
  responsavelEmail: string;
  tags?: string[];
}
