import { ILivreur } from 'app/shared/model/livreur.model';

export interface ICooperative {
  id?: number;
  nom?: string;
  directeur?: string | null;
  livreurs?: ILivreur[] | null;
}

export const defaultValue: Readonly<ICooperative> = {};
