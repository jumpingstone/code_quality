import React, { Component } from 'react';
import List from '@material-ui/core/List';
import ProjectItem from './ProjectItem';
import { API_URL } from './Util';

class ProjectList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedIndex: -1,
            projects: [],
            selectedProject : {}
        }
    }

    handleListItemClick = (event, project) => {
        this.setState({selectedProject : project});
        if (this.props.onSelectedChanged) {
            this.props.onSelectedChanged(event, project);
        }
    };

    componentDidMount() {
        fetch(API_URL + '/projects')
            .then(response => response.json())
            .then(data => {this.setState({ projects: data }) });
    }

    render() {
        return (
            <List>
                {console.log(this.state.projects)}

                {this.state.projects.map((project, i) => <ProjectItem key={i}
                    project={project} 
                    selected={project.name === this.state.selectedProject.name}
                    onSelected={this.handleListItemClick}/>)}

            </List>
        );
    }
}

export default ProjectList;