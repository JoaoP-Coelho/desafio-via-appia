import { Priority } from '../enums/priority';
import { Status } from '../enums/status';

export interface StatsResponse {
  porStatus: Record<Status, number>;
  porPrioridade: Record<Priority, number>;
}
