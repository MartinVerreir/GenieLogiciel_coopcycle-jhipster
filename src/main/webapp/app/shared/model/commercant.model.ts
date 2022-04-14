import { IPanier } from 'app/shared/model/panier.model';

export interface ICommercant {
  id?: number;
  carte?: string | null;
  menus?: string | null;
  horaires?: number | null;
  adresse?: string;
  paniers?: IPanier[] | null;
}

export const defaultValue: Readonly<ICommercant> = {};
