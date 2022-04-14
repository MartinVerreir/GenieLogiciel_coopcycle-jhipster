import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILivreur } from 'app/shared/model/livreur.model';
import { searchEntities, getEntities } from './livreur.reducer';

export const Livreur = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const livreurList = useAppSelector(state => state.livreur.entities);
  const loading = useAppSelector(state => state.livreur.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const startSearching = e => {
    if (search) {
      dispatch(searchEntities({ query: search }));
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="livreur-heading" data-cy="LivreurHeading">
        <Translate contentKey="coopcycleApp.livreur.home.title">Livreurs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopcycleApp.livreur.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/livreur/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopcycleApp.livreur.home.createLabel">Create new Livreur</Translate>
          </Link>
        </div>
      </h2>
      <Row>
        <Col sm="12">
          <Form onSubmit={startSearching}>
            <FormGroup>
              <InputGroup>
                <Input
                  type="text"
                  name="search"
                  defaultValue={search}
                  onChange={handleSearch}
                  placeholder={translate('coopcycleApp.livreur.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
      </Row>
      <div className="table-responsive">
        {livreurList && livreurList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="coopcycleApp.livreur.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.livreur.nom">Nom</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.livreur.prenom">Prenom</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.livreur.tel">Tel</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.livreur.cooperative">Cooperative</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {livreurList.map((livreur, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/livreur/${livreur.id}`} color="link" size="sm">
                      {livreur.id}
                    </Button>
                  </td>
                  <td>{livreur.nom}</td>
                  <td>{livreur.prenom}</td>
                  <td>{livreur.tel}</td>
                  <td>{livreur.cooperative ? <Link to={`/cooperative/${livreur.cooperative.id}`}>{livreur.cooperative.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/livreur/${livreur.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/livreur/${livreur.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/livreur/${livreur.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="coopcycleApp.livreur.home.notFound">No Livreurs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Livreur;
