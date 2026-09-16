import { Priority, Status } from '../models';

export interface IncidentFormValue {
  titulo: string;
  descricao: string;
  prioridade: Priority;
  status: Status;
  responsavelEmail: string;
  tags: string;
}

export interface NormalizedIncidentFormValue {
  titulo: string;
  descricao: string;
  prioridade: Priority;
  status: Status;
  responsavelEmail: string;
  tags: string[];
}

export function normalizeIncidentFormValue(
  formValue: IncidentFormValue
): NormalizedIncidentFormValue {
  const tags = formValue.tags
    .split(',')
    .map(tag => tag.trim().toLowerCase())
    .filter(Boolean)
    .filter((tag, index, allTags) => allTags.indexOf(tag) === index)
    .sort();

  return {
    titulo: formValue.titulo.trim(),
    descricao: formValue.descricao.trim(),
    prioridade: formValue.prioridade.toUpperCase() as Priority,
    status: formValue.status.toUpperCase() as Status,
    responsavelEmail: formValue.responsavelEmail.trim().toLowerCase(),
    tags
  };
}
