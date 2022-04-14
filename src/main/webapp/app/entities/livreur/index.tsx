import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Livreur from './livreur';
import LivreurDetail from './livreur-detail';
import LivreurUpdate from './livreur-update';
import LivreurDeleteDialog from './livreur-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LivreurUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LivreurUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LivreurDetail} />
      <ErrorBoundaryRoute path={match.url} component={Livreur} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LivreurDeleteDialog} />
  </>
);

export default Routes;
