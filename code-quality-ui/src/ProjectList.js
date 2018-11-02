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
            selectedProjectName : ''
        }
    }

    handleListItemClick = (event, projectName) => {
        this.setState({selectedProjectName : projectName});
    };

    componentDidMount() {
        fetch(API_URL + '/projects').
            then(response => response.json())
            .then(data => { console.log('setting state'); console.log(data); this.setState({ projects: data }) });
    }

    render() {
        return (
            <List>
                {console.log(this.state.projects)}

                {this.state.projects.map((project, i) => <ProjectItem key={i}
                    project={project} 
                    selected={project.name === this.state.selectedProjectName}
                    onSelected={this.handleListItemClick}/>)}

            </List>
        );
    }
}

export default ProjectList;