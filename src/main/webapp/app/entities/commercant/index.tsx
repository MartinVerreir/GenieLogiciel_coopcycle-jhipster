import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Commercant from './commercant';
import CommercantDetail from './commercant-detail';
import CommercantUpdate from './commercant-update';
import CommercantDeleteDialog from './commercant-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CommercantUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CommercantUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CommercantDetail} />
      <ErrorBoundaryRoute path={match.url} component={Commercant} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CommercantDeleteDialog} />
  </>
);

export default Routes;
