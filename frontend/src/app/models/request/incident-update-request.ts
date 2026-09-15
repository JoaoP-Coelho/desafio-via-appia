import { Priority } from '../enums/priority';
import { Status } from '../enums/status';

export interface IncidentUpdateRequest {
  titulo: string;
  descricao?: string;
  prioridade: Priority;
  responsavelEmail: string;
  status: Status;
  tags?: string[];
}
