import { Priority } from '../enums/priority';
import { Status } from '../enums/status';

export interface IncidentResponse {
  id: string;
  titulo: string;
  descricao: string | null;
  prioridade: Priority;
  status: Status;
  autor: string;
  responsavelEmail: string;
  tags: string[];
  dataAbertura: string;
  dataAtualizacao: string;
}
