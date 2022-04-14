import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/client">
        <Translate contentKey="global.menu.entities.client" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/panier">
        <Translate contentKey="global.menu.entities.panier" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/paiement">
        <Translate contentKey="global.menu.entities.paiement" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/livreur">
        <Translate contentKey="global.menu.entities.livreur" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cooperative">
        <Translate contentKey="global.menu.entities.cooperative" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/commercant">
        <Translate contentKey="global.menu.entities.commercant" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/commande">
        <Translate contentKey="global.menu.entities.commande" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
